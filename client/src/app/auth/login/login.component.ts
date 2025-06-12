import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent  implements OnInit, OnDestroy{

  loginResult = '';
  languageControl = this.fb.control('es');
  languages = [ 'en','es', 'fr', 'pt'];

  loginForm!: FormGroup;
  subscription = new Subscription();

  hidePassword: boolean = true;

  constructor(private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly translate: TranslateService) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    if (!this.languageControl.value) {
        this.languageControl.setValue('es');
    }
  }

  get email() {
    return this.loginForm.get('email')!;
  }

  get password() {
    return this.loginForm.get('password')!;
  }


  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    const { email, password } = this.loginForm.value;
    if (!email || !password) {
      this.loginResult = 'Por favor completa todos los campos';
      return;
    }
    this.loginResult = '';

    this.subscription.add(this.authService.login(email, password).subscribe({
      next: (res) => {
        alert('Login exitoso');
        console.log('Respuesta backend:', res);
      },
      error: (err) => {
        this.loginResult = 'Error en autenticación: ' + (err.error?.message || 'Intenta de nuevo');
      },
    }));
  }

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }

  switchLanguage(language: string) {
    this.languageControl.setValue(language);
    this.translate.use(language);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
