import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listBaseHall = (current: Number, limit: Number, queryParam: {}) => POST(`/baseHall/cms/${current}/${limit}`, queryParam)
export const  postBaseHall = (params: {}) => POST(`/baseHall/cms`, params)
export const  putBaseHall = (params: {}) => PUT(`/baseHall/cms`, params)
export const  deleteBaseHallByIds = (batch: {}) => DELETE(`/baseHall/cms/batch`, batch)
export const  getBaseHallConditions = () => GET(`/baseHall/cms/conditions`)
export const  getTableColumns = () => GET(`/baseHall/cms/table-columns`)
export const  getBaseHallSelector = () => GET(`/baseHall/cms/selector`)
