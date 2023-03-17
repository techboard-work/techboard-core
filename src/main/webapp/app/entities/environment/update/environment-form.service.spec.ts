import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../environment.test-samples';

import { EnvironmentFormService } from './environment-form.service';

describe('Environment Form Service', () => {
  let service: EnvironmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnvironmentFormService);
  });

  describe('Service methods', () => {
    describe('createEnvironmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEnvironmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            label: expect.any(Object),
            description: expect.any(Object),
            color: expect.any(Object),
            level: expect.any(Object),
            link: expect.any(Object),
          })
        );
      });

      it('passing IEnvironment should create a new form with FormGroup', () => {
        const formGroup = service.createEnvironmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            label: expect.any(Object),
            description: expect.any(Object),
            color: expect.any(Object),
            level: expect.any(Object),
            link: expect.any(Object),
          })
        );
      });
    });

    describe('getEnvironment', () => {
      it('should return NewEnvironment for default Environment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEnvironmentFormGroup(sampleWithNewData);

        const environment = service.getEnvironment(formGroup) as any;

        expect(environment).toMatchObject(sampleWithNewData);
      });

      it('should return NewEnvironment for empty Environment initial value', () => {
        const formGroup = service.createEnvironmentFormGroup();

        const environment = service.getEnvironment(formGroup) as any;

        expect(environment).toMatchObject({});
      });

      it('should return IEnvironment', () => {
        const formGroup = service.createEnvironmentFormGroup(sampleWithRequiredData);

        const environment = service.getEnvironment(formGroup) as any;

        expect(environment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEnvironment should not enable id FormControl', () => {
        const formGroup = service.createEnvironmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEnvironment should disable id FormControl', () => {
        const formGroup = service.createEnvironmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
