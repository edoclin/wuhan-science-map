import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listUserRegisterCourseByCourseId = (current: Number, limit: Number, queryParam: {}) => POST(`/userRegisterCourse/cms/${current}/${limit}`, queryParam)
export const  postUserRegisterCourse = (params: {}) => POST(`/userRegisterCourse/cms`, params)
export const  putUserRegisterCourse = (params: {}) => PUT(`/userRegisterCourse/cms`, params)
export const  deleteUserRegisterCourseByIds = (batch: {}) => DELETE(`/userRegisterCourse/cms/batch`, batch)
export const  getUserRegisterCourseConditions = () => GET(`/userRegisterCourse/cms/conditions`)
export const  getTableColumns = () => GET(`/userRegisterCourse/cms/table-columns`)
export const  getUserRegisterCourseSelector = () => GET(`/userRegisterCourse/cms/selector`)
