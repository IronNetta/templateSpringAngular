import {Component, EventEmitter, inject, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth-service.service';
import {FloatLabel} from 'primeng/floatlabel';
import {Password} from 'primeng/password';
import {Button} from 'primeng/button';
import {InputText} from 'primeng/inputtext';
import {PrimeTemplate} from 'primeng/api';
import {RouterLink} from '@angular/router';
import {Dialog} from 'primeng/dialog';
import {RegisterComponent} from '../register/register.component';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    FloatLabel,
    Password,
    Button,
    InputText,
    PrimeTemplate,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private readonly _fb: FormBuilder = inject(FormBuilder);
  private readonly _authService: AuthService = inject(AuthService);

  @Output()
  readonly close: EventEmitter<void> = new EventEmitter();

  @Output()
  private readonly _switchForm: EventEmitter<void> = new EventEmitter<void>();


  loginForm: FormGroup;
  errorMessage: string | null = null;
  isLoading = false;

  constructor() {
    this.loginForm = this._fb.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
    });
  }

  submit(): void {
    this.loginForm.markAllAsTouched();

    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading = true;
    this._authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.closeForm();
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
        this.errorMessage = err.error;
      }
    });
  }

  closeForm(): void {
    this.loginForm.reset();
    this.errorMessage = null;
    this.close.emit();
  }
  switchForm(): void {
    this.loginForm.reset();
    this.errorMessage = null;
    this._switchForm.emit();
  }
}
