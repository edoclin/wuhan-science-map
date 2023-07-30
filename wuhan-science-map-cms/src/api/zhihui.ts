import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listZhihuiTrip = (current: Number, limit: Number, queryParam: {}) => POST(`/zhihuiTrip/cms/${current}/${limit}`, queryParam)
export const  postZhihuiTrip = (params: {}) => POST(`/zhihuiTrip/cms`, params)
export const  putZhihuiTrip = (params: {}) => PUT(`/zhihuiTrip/cms`, params)
export const  deleteZhihuiTripByIds = (batch: {}) => DELETE(`/zhihuiTrip/cms/batch`, batch)
export const  getZhihuiTripConditions = () => GET(`/zhihuiTrip/cms/conditions`)
export const  getTableColumns = () => GET(`/zhihuiTrip/cms/table-columns`)
export const  getZhihuiTripSelector = () => GET(`/zhihuiTrip/cms/selector`)
