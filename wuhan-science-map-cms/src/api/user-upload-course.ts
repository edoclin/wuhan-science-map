import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listUserUploadCourse = (current: Number, limit: Number, queryParam: {}) => POST(`/userUploadCourse/cms/${current}/${limit}`, queryParam)
export const  postUserUploadCourse = (params: {}) => POST(`/userUploadCourse/cms`, params)
export const  putUserUploadCourse = (params: {}) => PUT(`/userUploadCourse/cms`, params)
export const  deleteUserUploadCourseByIds = (batch: {}) => DELETE(`/userUploadCourse/cms/batch`, batch)
export const  getUserUploadCourseConditions = () => GET(`/userUploadCourse/cms/conditions`)
export const  getTableColumns = () => GET(`/userUploadCourse/cms/table-columns`)
export const  getUserUploadCourseSelector = () => GET(`/userUploadCourse/cms/selector`)
