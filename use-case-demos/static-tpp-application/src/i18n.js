import I18NextHttpBackend from "i18next-http-backend";
import i18n from "i18next";
import {initReactI18next} from "react-i18next";

i18n.use(I18NextHttpBackend).use(initReactI18next).init({
    fallbackLng: 'en',
    ns: ['translation'],
    defaultNS: 'translation',

    backend: {
        loadPath: '/locales/{{lng}}/{{ns}}.json',
    },

    react:{
        useSuspense: true,
    },
    debug: true,
    interpolation: {
        escapeValue: false
    }
})

export default i18n;
