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

    // ✅ FIX 1: Use 'bulbul:v2'
    // ✅ FIX 2: Use a valid speaker like 'anushka' (Female) or 'abhilash' (Male)
    const body = {
      inputs: [text], // "inputs" is correct for the v2 API
      target_language_code: 'te-IN',
      speaker: 'anushka', // 'meera' was invalid
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
        // Log the full error to see details if it fails again
        console.error('Sarvam TTS API Error:', error); 
        return of(null);
      })
    );
  }
}