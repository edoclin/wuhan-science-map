import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listUser = (current: Number, limit: Number, queryParam: {}) => POST(`/user/cms/${current}/${limit}`, queryParam)
export const  postUser = (params: {}) => POST(`/user/cms`, params)
export const  putUser = (params: {}) => PUT(`/user/cms`, params)
export const  deleteUserByIds = (batch: {}) => DELETE(`/user/cms/batch`, batch)
export const  getUserConditions = () => GET(`/user/cms/conditions`)
export const  getTableColumns = () => GET(`/user/cms/table-columns`)
export const  getUserSelector = () => GET(`/user/cms/selector`)

export const  webLogin = (params: {}) => POST(`/user/login`, params)
export const  webCheck = () => GET(`/user/check`)
export const  webLogout = () => GET(`/user/logout`)
export const  listUserSelector = () => GET(`/user/web/selector`)
export const  webChangePassword = (param: {}) => PUT(`/user/change-password`, param)
