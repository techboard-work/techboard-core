import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ActivityFormService } from './activity-form.service';
import { ActivityService } from '../service/activity.service';
import { IActivity } from '../activity.model';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { EnvironmentService } from 'app/entities/environment/service/environment.service';
import { IActivityKind } from 'app/entities/activity-kind/activity-kind.model';
import { ActivityKindService } from 'app/entities/activity-kind/service/activity-kind.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ActivityUpdateComponent } from './activity-update.component';

describe('Activity Management Update Component', () => {
  let comp: ActivityUpdateComponent;
  let fixture: ComponentFixture<ActivityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let activityFormService: ActivityFormService;
  let activityService: ActivityService;
  let environmentService: EnvironmentService;
  let activityKindService: ActivityKindService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ActivityUpdateComponent],
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
      .overrideTemplate(ActivityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActivityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    activityFormService = TestBed.inject(ActivityFormService);
    activityService = TestBed.inject(ActivityService);
    environmentService = TestBed.inject(EnvironmentService);
    activityKindService = TestBed.inject(ActivityKindService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Environment query and add missing value', () => {
      const activity: IActivity = { id: 456 };
      const environment: IEnvironment = { id: 90968 };
      activity.environment = environment;

      const environmentCollection: IEnvironment[] = [{ id: 23052 }];
      jest.spyOn(environmentService, 'query').mockReturnValue(of(new HttpResponse({ body: environmentCollection })));
      const additionalEnvironments = [environment];
      const expectedCollection: IEnvironment[] = [...additionalEnvironments, ...environmentCollection];
      jest.spyOn(environmentService, 'addEnvironmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      expect(environmentService.query).toHaveBeenCalled();
      expect(environmentService.addEnvironmentToCollectionIfMissing).toHaveBeenCalledWith(
        environmentCollection,
        ...additionalEnvironments.map(expect.objectContaining)
      );
      expect(comp.environmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ActivityKind query and add missing value', () => {
      const activity: IActivity = { id: 456 };
      const kind: IActivityKind = { id: 29632 };
      activity.kind = kind;

      const activityKindCollection: IActivityKind[] = [{ id: 55253 }];
      jest.spyOn(activityKindService, 'query').mockReturnValue(of(new HttpResponse({ body: activityKindCollection })));
      const additionalActivityKinds = [kind];
      const expectedCollection: IActivityKind[] = [...additionalActivityKinds, ...activityKindCollection];
      jest.spyOn(activityKindService, 'addActivityKindToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      expect(activityKindService.query).toHaveBeenCalled();
      expect(activityKindService.addActivityKindToCollectionIfMissing).toHaveBeenCalledWith(
        activityKindCollection,
        ...additionalActivityKinds.map(expect.objectContaining)
      );
      expect(comp.activityKindsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const activity: IActivity = { id: 456 };
      const owner: IUser = { id: 'a9c94eac-684b-44e8-988c-5acb48999523' };
      activity.owner = owner;

      const userCollection: IUser[] = [{ id: '051254da-c148-4052-9e14-82fe35d0dcd7' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [owner];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const activity: IActivity = { id: 456 };
      const environment: IEnvironment = { id: 53461 };
      activity.environment = environment;
      const kind: IActivityKind = { id: 84548 };
      activity.kind = kind;
      const owner: IUser = { id: '1aae7534-177d-4e49-bd8d-a7a32406296e' };
      activity.owner = owner;

      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      expect(comp.environmentsSharedCollection).toContain(environment);
      expect(comp.activityKindsSharedCollection).toContain(kind);
      expect(comp.usersSharedCollection).toContain(owner);
      expect(comp.activity).toEqual(activity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivity>>();
      const activity = { id: 123 };
      jest.spyOn(activityFormService, 'getActivity').mockReturnValue(activity);
      jest.spyOn(activityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activity }));
      saveSubject.complete();

      // THEN
      expect(activityFormService.getActivity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(activityService.update).toHaveBeenCalledWith(expect.objectContaining(activity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivity>>();
      const activity = { id: 123 };
      jest.spyOn(activityFormService, 'getActivity').mockReturnValue({ id: null });
      jest.spyOn(activityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activity }));
      saveSubject.complete();

      // THEN
      expect(activityFormService.getActivity).toHaveBeenCalled();
      expect(activityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivity>>();
      const activity = { id: 123 };
      jest.spyOn(activityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(activityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEnvironment', () => {
      it('Should forward to environmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(environmentService, 'compareEnvironment');
        comp.compareEnvironment(entity, entity2);
        expect(environmentService.compareEnvironment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareActivityKind', () => {
      it('Should forward to activityKindService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(activityKindService, 'compareActivityKind');
        comp.compareActivityKind(entity, entity2);
        expect(activityKindService.compareActivityKind).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
