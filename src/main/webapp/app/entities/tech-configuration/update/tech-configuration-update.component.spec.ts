import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TechConfigurationFormService } from './tech-configuration-form.service';
import { TechConfigurationService } from '../service/tech-configuration.service';
import { ITechConfiguration } from '../tech-configuration.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TechConfigurationUpdateComponent } from './tech-configuration-update.component';

describe('TechConfiguration Management Update Component', () => {
  let comp: TechConfigurationUpdateComponent;
  let fixture: ComponentFixture<TechConfigurationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let techConfigurationFormService: TechConfigurationFormService;
  let techConfigurationService: TechConfigurationService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TechConfigurationUpdateComponent],
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
      .overrideTemplate(TechConfigurationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechConfigurationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    techConfigurationFormService = TestBed.inject(TechConfigurationFormService);
    techConfigurationService = TestBed.inject(TechConfigurationService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const techConfiguration: ITechConfiguration = { id: 456 };
      const author: IUser = { id: 'a9af1ccc-5138-4c23-aae0-ba7c1f6594c5' };
      techConfiguration.author = author;

      const userCollection: IUser[] = [{ id: 'a1b7e53c-6bf3-4573-9cbd-54b48e8f5fbb' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [author];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ techConfiguration });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const techConfiguration: ITechConfiguration = { id: 456 };
      const author: IUser = { id: '8d60e739-3841-4e2a-9429-c36294f05c4a' };
      techConfiguration.author = author;

      activatedRoute.data = of({ techConfiguration });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(author);
      expect(comp.techConfiguration).toEqual(techConfiguration);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechConfiguration>>();
      const techConfiguration = { id: 123 };
      jest.spyOn(techConfigurationFormService, 'getTechConfiguration').mockReturnValue(techConfiguration);
      jest.spyOn(techConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techConfiguration }));
      saveSubject.complete();

      // THEN
      expect(techConfigurationFormService.getTechConfiguration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(techConfigurationService.update).toHaveBeenCalledWith(expect.objectContaining(techConfiguration));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechConfiguration>>();
      const techConfiguration = { id: 123 };
      jest.spyOn(techConfigurationFormService, 'getTechConfiguration').mockReturnValue({ id: null });
      jest.spyOn(techConfigurationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techConfiguration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techConfiguration }));
      saveSubject.complete();

      // THEN
      expect(techConfigurationFormService.getTechConfiguration).toHaveBeenCalled();
      expect(techConfigurationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechConfiguration>>();
      const techConfiguration = { id: 123 };
      jest.spyOn(techConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(techConfigurationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
