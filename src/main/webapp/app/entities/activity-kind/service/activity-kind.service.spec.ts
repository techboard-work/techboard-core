import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IActivityKind } from '../activity-kind.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../activity-kind.test-samples';

import { ActivityKindService } from './activity-kind.service';

const requireRestSample: IActivityKind = {
  ...sampleWithRequiredData,
};

describe('ActivityKind Service', () => {
  let service: ActivityKindService;
  let httpMock: HttpTestingController;
  let expectedResult: IActivityKind | IActivityKind[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ActivityKindService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ActivityKind', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const activityKind = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(activityKind).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ActivityKind', () => {
      const activityKind = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(activityKind).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ActivityKind', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ActivityKind', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ActivityKind', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addActivityKindToCollectionIfMissing', () => {
      it('should add a ActivityKind to an empty array', () => {
        const activityKind: IActivityKind = sampleWithRequiredData;
        expectedResult = service.addActivityKindToCollectionIfMissing([], activityKind);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activityKind);
      });

      it('should not add a ActivityKind to an array that contains it', () => {
        const activityKind: IActivityKind = sampleWithRequiredData;
        const activityKindCollection: IActivityKind[] = [
          {
            ...activityKind,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addActivityKindToCollectionIfMissing(activityKindCollection, activityKind);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ActivityKind to an array that doesn't contain it", () => {
        const activityKind: IActivityKind = sampleWithRequiredData;
        const activityKindCollection: IActivityKind[] = [sampleWithPartialData];
        expectedResult = service.addActivityKindToCollectionIfMissing(activityKindCollection, activityKind);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activityKind);
      });

      it('should add only unique ActivityKind to an array', () => {
        const activityKindArray: IActivityKind[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const activityKindCollection: IActivityKind[] = [sampleWithRequiredData];
        expectedResult = service.addActivityKindToCollectionIfMissing(activityKindCollection, ...activityKindArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const activityKind: IActivityKind = sampleWithRequiredData;
        const activityKind2: IActivityKind = sampleWithPartialData;
        expectedResult = service.addActivityKindToCollectionIfMissing([], activityKind, activityKind2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activityKind);
        expect(expectedResult).toContain(activityKind2);
      });

      it('should accept null and undefined values', () => {
        const activityKind: IActivityKind = sampleWithRequiredData;
        expectedResult = service.addActivityKindToCollectionIfMissing([], null, activityKind, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activityKind);
      });

      it('should return initial array if no ActivityKind is added', () => {
        const activityKindCollection: IActivityKind[] = [sampleWithRequiredData];
        expectedResult = service.addActivityKindToCollectionIfMissing(activityKindCollection, undefined, null);
        expect(expectedResult).toEqual(activityKindCollection);
      });
    });

    describe('compareActivityKind', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareActivityKind(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareActivityKind(entity1, entity2);
        const compareResult2 = service.compareActivityKind(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareActivityKind(entity1, entity2);
        const compareResult2 = service.compareActivityKind(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareActivityKind(entity1, entity2);
        const compareResult2 = service.compareActivityKind(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
