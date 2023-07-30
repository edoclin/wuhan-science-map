import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listLecturer = (current: Number, limit: Number, queryParam: {}) => POST(`/lecturer/cms/${current}/${limit}`, queryParam)
export const  postLecturer = (params: {}) => POST(`/lecturer/cms`, params)
export const  putLecturer = (params: {}) => PUT(`/lecturer/cms`, params)
export const  deleteLecturerByIds = (batch: {}) => DELETE(`/lecturer/cms/batch`, batch)
export const  getLecturerConditions = () => GET(`/lecturer/cms/conditions`)
export const  getTableColumns = () => GET(`/lecturer/cms/table-columns`)
export const  getLecturerSelector = () => GET(`/lecturer/cms/selector`)
