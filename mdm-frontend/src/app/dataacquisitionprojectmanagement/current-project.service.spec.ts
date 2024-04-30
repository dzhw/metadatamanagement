import { TestBed } from '@angular/core/testing';

import { CurrentProjectService } from './current-project.service';

describe('CurrentProjectService', () => {
  let service: CurrentProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurrentProjectService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
