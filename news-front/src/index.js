import React from 'react';
import ReactDOM from 'react-dom';

import App from './App';

import {I18nextProvider} from 'react-i18next';
import i18next from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

import translation_EN from './locales/en/translation.json';
import translation_RU from './locales/ru/translation.json';

const options = {
	// order and from where user language should be detected
	order: [ 'cookie', 'querystring', 'localStorage', 'path', 'subdomain'],
  
	// keys or params to lookup language from
	lookupQuerystring: 'lng',
	lookupCookie: 'i18next',
	lookupLocalStorage: 'i18nextLng',
	lookupFromPathIndex: 0,
	lookupFromSubdomainIndex: 0,
  
	// cache user language on
	caches: ['localStorage', 'cookie'],
	excludeCacheFor: ['cimode'], // languages to not persist (cookie, localStorage)
  
	// optional expire and domain for set cookie
	cookieMinutes: 0,
	cookieDomain: 'myDomain',
  
	// optional htmlTag with lang attribute, the default is:
	htmlTag: document.documentElement,
  
	// only detect languages that are in the whitelist
	checkWhitelist: true
  }

i18next
	.use(LanguageDetector)
	.init({
	interpolation: { escapeValue: false },   
	detection: options,                          
	resources: {
		en: {
			translation: translation_EN              
		},
		ru: {
			translation: translation_RU
		},
	},
});

// ========================================

ReactDOM.render(
	<I18nextProvider i18n={i18next}>
		<App/>
	</I18nextProvider>,
    document.getElementById('root')
);
