import { boot } from 'quasar/wrappers'
import {initAMapApiLoader} from '@vuemap/vue-amap';
import '@vuemap/vue-amap/dist/style.css'


// TODO
export const amapJsKey = ''
export const amapWebKey = ''
initAMapApiLoader({
    key: amapJsKey,
    securityJsCode: ''
})
export default boot(async ({ app}) => {
})
