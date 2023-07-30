import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listZhihuiItem = (current: Number, limit: Number, queryParam: {}) => POST(`/zhihuiTripItem/cms/${current}/${limit}`, queryParam)
export const  postZhihuiItem = (params: {}) => POST(`/zhihuiTripItem/cms`, params)
export const  putZhihuiItem = (params: {}) => PUT(`/zhihuiTripItem/cms`, params)
export const  deleteZhihuiItemByIds = (batch: {}) => DELETE(`/zhihuiTripItem/cms/batch`, batch)
export const  getZhihuiItemConditions = () => GET(`/zhihuiTripItem/cms/conditions`)
export const  getTableColumns = () => GET(`/zhihuiTripItem/cms/table-columns`)
export const  getZhihuiItemSelector = () => GET(`/zhihuiTripItem/cms/selector`)
