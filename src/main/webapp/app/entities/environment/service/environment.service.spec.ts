import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEnvironment } from '../environment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../environment.test-samples';

import { EnvironmentService } from './environment.service';

const requireRestSample: IEnvironment = {
  ...sampleWithRequiredData,
};

describe('Environment Service', () => {
  let service: EnvironmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IEnvironment | IEnvironment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EnvironmentService);
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

    it('should create a Environment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const environment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(environment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Environment', () => {
      const environment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(environment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Environment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Environment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Environment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEnvironmentToCollectionIfMissing', () => {
      it('should add a Environment to an empty array', () => {
        const environment: IEnvironment = sampleWithRequiredData;
        expectedResult = service.addEnvironmentToCollectionIfMissing([], environment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(environment);
      });

      it('should not add a Environment to an array that contains it', () => {
        const environment: IEnvironment = sampleWithRequiredData;
        const environmentCollection: IEnvironment[] = [
          {
            ...environment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEnvironmentToCollectionIfMissing(environmentCollection, environment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Environment to an array that doesn't contain it", () => {
        const environment: IEnvironment = sampleWithRequiredData;
        const environmentCollection: IEnvironment[] = [sampleWithPartialData];
        expectedResult = service.addEnvironmentToCollectionIfMissing(environmentCollection, environment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(environment);
      });

      it('should add only unique Environment to an array', () => {
        const environmentArray: IEnvironment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const environmentCollection: IEnvironment[] = [sampleWithRequiredData];
        expectedResult = service.addEnvironmentToCollectionIfMissing(environmentCollection, ...environmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const environment: IEnvironment = sampleWithRequiredData;
        const environment2: IEnvironment = sampleWithPartialData;
        expectedResult = service.addEnvironmentToCollectionIfMissing([], environment, environment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(environment);
        expect(expectedResult).toContain(environment2);
      });

      it('should accept null and undefined values', () => {
        const environment: IEnvironment = sampleWithRequiredData;
        expectedResult = service.addEnvironmentToCollectionIfMissing([], null, environment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(environment);
      });

      it('should return initial array if no Environment is added', () => {
        const environmentCollection: IEnvironment[] = [sampleWithRequiredData];
        expectedResult = service.addEnvironmentToCollectionIfMissing(environmentCollection, undefined, null);
        expect(expectedResult).toEqual(environmentCollection);
      });
    });

    describe('compareEnvironment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEnvironment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEnvironment(entity1, entity2);
        const compareResult2 = service.compareEnvironment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEnvironment(entity1, entity2);
        const compareResult2 = service.compareEnvironment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEnvironment(entity1, entity2);
        const compareResult2 = service.compareEnvironment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
