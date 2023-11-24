import { TestBed } from '@angular/core/testing';

import { DataacquisitionprojectSearchService } from './dataacquisitionproject.search.service';

describe('DataacquisitionprojectSearchService', () => {
  let service: DataacquisitionprojectSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DataacquisitionprojectSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
