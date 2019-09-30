import { TestBed } from '@angular/core/testing';

import { WordIntegrationServiceService } from './word-integration-service.service';

describe('WordIntegrationServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WordIntegrationServiceService = TestBed.get(WordIntegrationServiceService);
    expect(service).toBeTruthy();
  });
});
