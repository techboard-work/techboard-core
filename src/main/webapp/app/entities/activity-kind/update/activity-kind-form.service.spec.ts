import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../activity-kind.test-samples';

import { ActivityKindFormService } from './activity-kind-form.service';

describe('ActivityKind Form Service', () => {
  let service: ActivityKindFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ActivityKindFormService);
  });

  describe('Service methods', () => {
    describe('createActivityKindFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createActivityKindFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            color: expect.any(Object),
            icon: expect.any(Object),
          })
        );
      });

      it('passing IActivityKind should create a new form with FormGroup', () => {
        const formGroup = service.createActivityKindFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            color: expect.any(Object),
            icon: expect.any(Object),
          })
        );
      });
    });

    describe('getActivityKind', () => {
      it('should return NewActivityKind for default ActivityKind initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createActivityKindFormGroup(sampleWithNewData);

        const activityKind = service.getActivityKind(formGroup) as any;

        expect(activityKind).toMatchObject(sampleWithNewData);
      });

      it('should return NewActivityKind for empty ActivityKind initial value', () => {
        const formGroup = service.createActivityKindFormGroup();

        const activityKind = service.getActivityKind(formGroup) as any;

        expect(activityKind).toMatchObject({});
      });

      it('should return IActivityKind', () => {
        const formGroup = service.createActivityKindFormGroup(sampleWithRequiredData);

        const activityKind = service.getActivityKind(formGroup) as any;

        expect(activityKind).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IActivityKind should not enable id FormControl', () => {
        const formGroup = service.createActivityKindFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewActivityKind should disable id FormControl', () => {
        const formGroup = service.createActivityKindFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
