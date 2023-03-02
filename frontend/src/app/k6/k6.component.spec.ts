import { ComponentFixture, TestBed } from '@angular/core/testing';

import { K6Component } from './k6.component';

describe('K6Component', () => {
  let component: K6Component;
  let fixture: ComponentFixture<K6Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ K6Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(K6Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
