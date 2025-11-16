import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoviesSplashComponent } from './movies-splash.component';

describe('MoviesSplashComponent', () => {
  let component: MoviesSplashComponent;
  let fixture: ComponentFixture<MoviesSplashComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoviesSplashComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoviesSplashComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
