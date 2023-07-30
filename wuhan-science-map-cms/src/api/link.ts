import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listLink = (current: Number, limit: Number, queryParam: {}) => POST(`/link/cms/${current}/${limit}`, queryParam)
export const  postLink = (params: {}) => POST(`/link/cms`, params)
export const  putLink = (params: {}) => PUT(`/link/cms`, params)
export const  deleteLinkByIds = (batch: {}) => DELETE(`/link/cms/batch`, batch)
export const  getLinkConditions = () => GET(`/link/cms/conditions`)
export const  getTableColumns = () => GET(`/link/cms/table-columns`)
export const  getLinkSelector = () => GET(`/link/cms/selector`)
