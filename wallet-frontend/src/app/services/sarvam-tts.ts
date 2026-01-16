import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environments';
import { catchError, map, of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SarvamTTSService {
  private apiUrl = 'https://api.sarvam.ai/text-to-speech';

  constructor(private http: HttpClient) {}

  speak(text: string) {
    if (!environment.sarvamApiKey) {
      console.warn('Sarvam API Key is missing! text-to-speech skipped.');
      return of(null);
    }

    
    
    const body = {
      inputs: [text], 
      target_language_code: 'te-IN',
      speaker: 'anushka', 
      pitch: 0,
      pace: 1.0,
      loudness: 1.5,
      speech_sample_rate: 16000,
      enable_preprocessing: true,
      model: 'bulbul:v2' 
    };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'api-subscription-key': environment.sarvamApiKey
    });

    return this.http.post<any>(this.apiUrl, body, { headers }).pipe(
      map(response => {
        // The API returns { audios: ["base64string..."] }
        if (response && response.audios && response.audios.length > 0) {
          return response.audios[0];
        }
        return null;
      }),
      catchError(error => {
        
        console.error('Sarvam TTS API Error:', error); 
        return of(null);
      })
    );
  }
}
