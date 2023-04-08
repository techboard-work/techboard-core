import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../activity.test-samples';

import { ActivityFormService } from './activity-form.service';

describe('Activity Form Service', () => {
  let service: ActivityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ActivityFormService);
  });

  describe('Service methods', () => {
    describe('createActivityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createActivityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            startedOn: expect.any(Object),
            finishedOn: expect.any(Object),
            link: expect.any(Object),
            severity: expect.any(Object),
            environment: expect.any(Object),
            kind: expect.any(Object),
            owner: expect.any(Object),
          })
        );
      });

      it('passing IActivity should create a new form with FormGroup', () => {
        const formGroup = service.createActivityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            startedOn: expect.any(Object),
            finishedOn: expect.any(Object),
            link: expect.any(Object),
            severity: expect.any(Object),
            environment: expect.any(Object),
            kind: expect.any(Object),
            owner: expect.any(Object),
          })
        );
      });
    });

    describe('getActivity', () => {
      it('should return NewActivity for default Activity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createActivityFormGroup(sampleWithNewData);

        const activity = service.getActivity(formGroup) as any;

        expect(activity).toMatchObject(sampleWithNewData);
      });

      it('should return NewActivity for empty Activity initial value', () => {
        const formGroup = service.createActivityFormGroup();

        const activity = service.getActivity(formGroup) as any;

        expect(activity).toMatchObject({});
      });

      it('should return IActivity', () => {
        const formGroup = service.createActivityFormGroup(sampleWithRequiredData);

        const activity = service.getActivity(formGroup) as any;

        expect(activity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IActivity should not enable id FormControl', () => {
        const formGroup = service.createActivityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewActivity should disable id FormControl', () => {
        const formGroup = service.createActivityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
