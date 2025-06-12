import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent  implements OnDestroy{

  errorMessage = '';

  selectedLanguage = 'es';

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  subscription = new Subscription();

  constructor(private fb: FormBuilder, private authService: AuthService) {}

  get email() {
    return this.loginForm.get('email')!;
  }

  get password() {
    return this.loginForm.get('password')!;
  }


  onSubmit() {
    if (this.loginForm.invalid) return;

    const { email, password } = this.loginForm.value;
    if (!email || !password) {
      this.errorMessage = 'Por favor completa todos los campos';
      return;
    }
    this.errorMessage = '';

    this.subscription.add(this.authService.login(email, password).subscribe({
      next: (res) => {
        alert('Login exitoso');
        console.log('Respuesta backend:', res);
      },
      error: (err) => {
        this.errorMessage = 'Error en autenticación: ' + (err.error?.message || 'Intenta de nuevo');
      },
    }));
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
