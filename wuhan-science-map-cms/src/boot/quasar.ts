import { boot } from 'quasar/wrappers'
import { EventBus, LocalStorage } from 'quasar'

export default boot(async ({ app}) => {
  const bus = new EventBus()
  app.provide('bus', bus)
  app.provide('localStorage', LocalStorage)
})
