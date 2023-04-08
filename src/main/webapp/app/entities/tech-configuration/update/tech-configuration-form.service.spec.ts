import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tech-configuration.test-samples';

import { TechConfigurationFormService } from './tech-configuration-form.service';

describe('TechConfiguration Form Service', () => {
  let service: TechConfigurationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechConfigurationFormService);
  });

  describe('Service methods', () => {
    describe('createTechConfigurationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechConfigurationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            version: expect.any(Object),
            timestamp: expect.any(Object),
            content: expect.any(Object),
            author: expect.any(Object),
          })
        );
      });

      it('passing ITechConfiguration should create a new form with FormGroup', () => {
        const formGroup = service.createTechConfigurationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            version: expect.any(Object),
            timestamp: expect.any(Object),
            content: expect.any(Object),
            author: expect.any(Object),
          })
        );
      });
    });

    describe('getTechConfiguration', () => {
      it('should return NewTechConfiguration for default TechConfiguration initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTechConfigurationFormGroup(sampleWithNewData);

        const techConfiguration = service.getTechConfiguration(formGroup) as any;

        expect(techConfiguration).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechConfiguration for empty TechConfiguration initial value', () => {
        const formGroup = service.createTechConfigurationFormGroup();

        const techConfiguration = service.getTechConfiguration(formGroup) as any;

        expect(techConfiguration).toMatchObject({});
      });

      it('should return ITechConfiguration', () => {
        const formGroup = service.createTechConfigurationFormGroup(sampleWithRequiredData);

        const techConfiguration = service.getTechConfiguration(formGroup) as any;

        expect(techConfiguration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechConfiguration should not enable id FormControl', () => {
        const formGroup = service.createTechConfigurationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechConfiguration should disable id FormControl', () => {
        const formGroup = service.createTechConfigurationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
