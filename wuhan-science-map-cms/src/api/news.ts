import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listNews = (current: Number, limit: Number, queryParam: {}) => POST(`/news/cms/${current}/${limit}`, queryParam)
export const  postNews = (params: {}) => POST(`/news/cms`, params)
export const  putNews = (params: {}) => PUT(`/news/cms`, params)
export const  deleteNewsByIds = (batch: {}) => DELETE(`/news/cms/batch`, batch)
export const  getNewsConditions = () => GET(`/news/cms/conditions`)
export const  getTableColumns = () => GET(`/news/cms/table-columns`)
export const  getNewsSelector = () => GET(`/news/cms/selector`)
export const  listNewsDisplayIndex = () => GET(`/news/cms/displayIndex`)
export const  putNewsDisplayIndex = (batch: Array<any>) => PUT(`/news/cms/changeIndex`, batch)

