import { TestBed } from '@angular/core/testing';

import { SarvamTts } from './sarvam-tts';

describe('SarvamTts', () => {
  let service: SarvamTts;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SarvamTts);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
