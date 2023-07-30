import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listActivity = (current: Number, limit: Number, queryParam: {}) => POST(`/activity/cms/${current}/${limit}`, queryParam)
export const  postActivity = (params: {}) => POST(`/activity/cms`, params)
export const  putActivity = (params: {}) => PUT(`/activity/cms`, params)
export const  deleteActivityByIds = (batch: {}) => DELETE(`/activity/cms/batch`, batch)
export const  getActivityConditions = () => GET(`/activity/cms/conditions`)
export const  getTableColumns = () => GET(`/activity/cms/table-columns`)
export const  getActivitySelector = () => GET(`/activity/cms/selector`)
