import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ActivityKindFormService } from './activity-kind-form.service';
import { ActivityKindService } from '../service/activity-kind.service';
import { IActivityKind } from '../activity-kind.model';

import { ActivityKindUpdateComponent } from './activity-kind-update.component';

describe('ActivityKind Management Update Component', () => {
  let comp: ActivityKindUpdateComponent;
  let fixture: ComponentFixture<ActivityKindUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let activityKindFormService: ActivityKindFormService;
  let activityKindService: ActivityKindService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ActivityKindUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ActivityKindUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActivityKindUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    activityKindFormService = TestBed.inject(ActivityKindFormService);
    activityKindService = TestBed.inject(ActivityKindService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const activityKind: IActivityKind = { id: 456 };

      activatedRoute.data = of({ activityKind });
      comp.ngOnInit();

      expect(comp.activityKind).toEqual(activityKind);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivityKind>>();
      const activityKind = { id: 123 };
      jest.spyOn(activityKindFormService, 'getActivityKind').mockReturnValue(activityKind);
      jest.spyOn(activityKindService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activityKind });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activityKind }));
      saveSubject.complete();

      // THEN
      expect(activityKindFormService.getActivityKind).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(activityKindService.update).toHaveBeenCalledWith(expect.objectContaining(activityKind));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivityKind>>();
      const activityKind = { id: 123 };
      jest.spyOn(activityKindFormService, 'getActivityKind').mockReturnValue({ id: null });
      jest.spyOn(activityKindService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activityKind: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activityKind }));
      saveSubject.complete();

      // THEN
      expect(activityKindFormService.getActivityKind).toHaveBeenCalled();
      expect(activityKindService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivityKind>>();
      const activityKind = { id: 123 };
      jest.spyOn(activityKindService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activityKind });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(activityKindService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
