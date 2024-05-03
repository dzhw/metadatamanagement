import { Component, Inject } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'mdm-frontend';
  localesList = [
    { code: 'en', label: 'English' },
    { code: 'de', label: 'Deutsch' }
  ];
}
