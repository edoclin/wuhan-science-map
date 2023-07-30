import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  list$module$ = (current: Number, limit: Number, queryParam: {}) => POST(`/$prefix$/cms/${current}/${limit}`, queryParam)
export const  post$module$ = (params: {}) => POST(`/$prefix$/cms`, params)
export const  put$module$ = (params: {}) => PUT(`/$prefix$/cms`, params)
export const  delete$module$ByIds = (batch: {}) => DELETE(`/$prefix$/cms/batch`, batch)
export const  get$module$Conditions = () => GET(`/$prefix$/cms/conditions`)
export const  getTableColumns = () => GET(`/$prefix$/cms/table-columns`)
export const  get$module$Selector = () => GET(`/$prefix$/cms/selector`)
