import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './login/login.component';
import { MyAngularMaterialModule } from '../my-angular-material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { SsoCallbackComponent } from './sso-callback/sso-callback.component';


@NgModule({
  declarations: [LoginComponent, SsoCallbackComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MyAngularMaterialModule,
    AuthRoutingModule,
    TranslateModule
  ]
})
export class AuthModule { }
