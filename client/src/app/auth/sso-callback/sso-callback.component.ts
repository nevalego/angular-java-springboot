import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-sso-callback',
  templateUrl: './sso-callback.component.html',
  styleUrls: ['./sso-callback.component.scss']
})
export class SsoCallbackComponent implements OnInit {

  message: string = 'GLOBAL.PROCESSING_SSO';
  isLoading: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      const state = params['state'];
      const redirectUri = 'http://localhost:4200/auth/sso-callback';

      if (code && state) {
        this.message = 'GLOBAL.VALIDATING_SSO';
        this.authService.processSsoCallback(code, state, redirectUri).subscribe({
          next: (response) => {
            this.isLoading = false;
            if (response.status === 'success' && response.token) {

              this.message = 'GLOBAL.SSO_SUCCESS';
              console.log('SSO login successful, token received.');

              setTimeout(() => {
                this.router.navigate(['/dashboard']);
              }, 1000);
            } else {
              this.message = response.message || 'GLOBAL.SSO_FAILED_GENERIC';
              console.error('SSO login failed:', response.message);
              setTimeout(() => {
                this.router.navigate(['/auth/login']);
              }, 2000);
            }
          },
          error: (err: Error) => {
            this.isLoading = false;
            this.message = err.message || 'GLOBAL.SSO_FAILED_GENERIC';
            console.error('Error processing SSO callback:', err);
            setTimeout(() => {
              this.router.navigate(['/auth/login']);
            }, 2000);
          }
        });
      } else {
        this.isLoading = false;
        this.message = 'GLOBAL.SSO_MISSING_PARAMS';
        console.error('SSO callback: Missing code or state parameters.');
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 2000);
      }
    });
  }
}
