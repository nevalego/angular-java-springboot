import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

interface LoginResponse {
  token: string;
  message?: string;
}

interface SsoCallbackSuccessResponse {
  status: 'success';
  message: string;
  token: string;
}

interface SsoCallbackErrorResponse {
  status: 'error';
  message: string;
}

type SsoCallbackApiResponse = SsoCallbackSuccessResponse | SsoCallbackErrorResponse;


@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private frontendBaseUrl = 'http://localhost:4200';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, { email, password })
    .pipe(catchError(this.handleError));
  }

  initiateSso(): Observable<any> {
    window.location.href = `${this.apiUrl}/auth/sso`;
    return new Observable();
  }

   processSsoCallback(code: string, state: string, redirectUri: string): Observable<SsoCallbackApiResponse> {
    return this.http.get<SsoCallbackApiResponse>(`${this.apiUrl}/sso/callback`, {
      params: { code, state, redirect_uri: redirectUri }
    }).pipe(
      tap(response => {
        if (response.status === 'success' && response.token) {
          localStorage.setItem('jwt_token', response.token);
          console.log('Token SSO recibido y guardado.');
        }
      }),
      catchError(this.handleError)
    );
  }


  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
      errorMessage = `Error: ${error.error.message}`;
    } else {
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${JSON.stringify(error.error)}`);

      if (error.status === 401) {
        errorMessage = 'Credenciales inválidas. Por favor, verifica tu correo y contraseña.';
      }else if (error.status === 400 && error.error && error.error.message && error.error.message.includes("SSO Callback")) {
        errorMessage = error.error.message;
      } else if (error.status >= 400 && error.status < 500) {
        errorMessage = `Error de cliente: ${error.status} - ${error.statusText}`;
        if (error.error && error.error.message) {
            errorMessage = error.error.message;
        }
      } else if (error.status >= 500) {
        errorMessage = `Error del servidor: ${error.status} - ${error.statusText}`;
        if (error.error && error.error.message) {
            errorMessage = error.error.message;
        }
      }
    }

    return throwError(() => new Error(errorMessage));
  }
}
