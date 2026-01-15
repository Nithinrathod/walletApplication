import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../services/toast';
import { SarvamTTSService } from '../../services/sarvam-tts'; // 1. Import New Service
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.html',
  styleUrls: ['./toast.css']
})
export class ToastComponent implements OnInit, OnDestroy {
  message = '';
  private timeoutId: any;
  private subscription: Subscription | undefined;
  private currentAudio: HTMLAudioElement | null = null; // 2. Track Audio Element

  constructor(
    private toastService: ToastService,
    private ttsService: SarvamTTSService // 3. Inject Service
  ) {}

  ngOnInit() {
    this.subscription = this.toastService.message$.subscribe(msg => {
      this.message = msg;
      
      if (msg) {
        if (this.timeoutId) clearTimeout(this.timeoutId);
        
        // Convert to Telugu Text
        const nativeText = this.getNativeTeluguText(msg);

        // Call Sarvam AI to speak
        this.playSarvamAudio(nativeText);
      }
    });
  }

  ngOnDestroy() {
    if (this.subscription) this.subscription.unsubscribe();
    this.close();
  }

  close() {
    this.message = ''; 
    this.stopAudio(); // Stop any playing audio
  }

  // Helper to stop current audio
  private stopAudio() {
    if (this.currentAudio) {
      this.currentAudio.pause();
      this.currentAudio.currentTime = 0;
      this.currentAudio = null;
    }
  }

  playSarvamAudio(text: string) {
    this.stopAudio(); // Stop previous before starting new

    // Call the API
    this.ttsService.speak(text).subscribe(base64Audio => {
      if (base64Audio) {
        // Create audio from Base64 string
        this.currentAudio = new Audio(`data:audio/wav;base64,${base64Audio}`);
        
        this.currentAudio.onended = () => {
           // Auto-close toast 1 second after audio finishes
           this.timeoutId = setTimeout(() => this.close(), 1000);
        };
        
        // Safety timeout (close if audio hangs or is too long)
        this.timeoutId = setTimeout(() => this.close(), 15000);

        this.currentAudio.play().catch(e => console.error("Audio Playback Error:", e));
      } else {
        // Fallback: Use browser voice if API fails
        console.log("Falling back to Browser TTS");
        this.speakBrowserFallback(text);
      }
    });
  }

  // Keep your existing Logic for Telugu Translation
  getNativeTeluguText(text: string): string {
    let cleanText = text.replace(/₹/g, ' ').replace(/:/g, ' ').replace(/,/g, '').trim();

    const amountMatch = cleanText.match(/(\d+(\.\d+)?)/);
    const amount = amountMatch ? Math.round(parseFloat(amountMatch[0])) : 0;
    const balanceMatch = cleanText.match(/Balance\s*(\d+(\.\d+)?)/i);
    const balanceText = balanceMatch ? `. Mee balance, ${Math.round(parseFloat(balanceMatch[1]))} Rūpāyilu.` : '';

    if (cleanText.match(/Credited|Received/i)) {
      const senderMatch = cleanText.match(/from\s+(.+?)(?:\s+Balance|$)/i);
      const sender = senderMatch ? senderMatch[1].trim() : "Okharu";
      return `${sender} nundi, ${amount} Rūpāyilu, jama ayyayi${balanceText}`;
    }
    if (cleanText.match(/Debited|Sent|Transfer/i)) {
      return `${amount} Rūpāyilu, vijayavantham-gaa Pampāru${balanceText}`;
    }
    if (cleanText.match(/Added|Deposit/i)) {
      return `Mee wallet lo, ${amount} Rūpāyilu, add cheybaddayyi${balanceText}`;
    }
    if (cleanText.includes("Insufficient")) return "Mee account lo saripada dabbulu levu.";
    if (cleanText.includes("Blocked")) return "Ee transaction aapi-vey-a-badindhi.";
    if (cleanText.includes("Failed")) return "Transaction viphalam ayindhi.";

    return cleanText.replace(/(\d+)/g, "$1 Rūpāyilu"); 
  }

  // Fallback to old method if API key is missing or request fails
  speakBrowserFallback(text: string) {
    if (typeof window === 'undefined' || !('speechSynthesis' in window)) return;
    window.speechSynthesis.cancel();
    const utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = 'te-IN'; 
    utterance.rate = 0.85; 
    utterance.onend = () => {
       this.timeoutId = setTimeout(() => this.close(), 1000);
    };
    window.speechSynthesis.speak(utterance);
  }
}