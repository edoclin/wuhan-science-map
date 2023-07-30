import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listBaseCourse = (current: Number, limit: Number, queryParam: {}) => POST(`/baseCourse/cms/${current}/${limit}`, queryParam)
export const  postBaseCourse = (params: {}) => POST(`/baseCourse/cms`, params)
export const  putBaseCourse = (params: {}) => PUT(`/baseCourse/cms`, params)
export const  deleteBaseCourseByIds = (batch: {}) => DELETE(`/baseCourse/cms/batch`, batch)
export const  getBaseCourseConditions = () => GET(`/baseCourse/cms/conditions`)
export const  getTableColumns = () => GET(`/baseCourse/cms/table-columns`)
export const  getBaseCourseSelector = () => GET(`/baseCourse/cms/selector`)
