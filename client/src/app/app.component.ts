import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'client';

  constructor(private translate: TranslateService) {
    translate.addLangs(['en', 'es','fr', 'pt']);
    translate.setDefaultLang('es');
    const browserLang = translate.getBrowserLang() || 'es';
    let langToUse = browserLang.match(/en|es|fr|pt/) ? browserLang : 'es';
    this.translate.use(langToUse);
  }
}
