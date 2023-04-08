import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TechConfigurationDetailComponent } from './tech-configuration-detail.component';

describe('TechConfiguration Management Detail Component', () => {
  let comp: TechConfigurationDetailComponent;
  let fixture: ComponentFixture<TechConfigurationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TechConfigurationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ techConfiguration: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TechConfigurationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TechConfigurationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load techConfiguration on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.techConfiguration).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
