import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listScienceBase = (current: Number, limit: Number, queryParam: {}) => POST(`/scienceBase/cms/${current}/${limit}`, queryParam)
export const  postScienceBase = (params: {}) => POST(`/scienceBase/cms`, params)
export const  putScienceBase = (params: {}) => PUT(`/scienceBase/cms`, params)
export const  deleteScienceBaseByIds = (batch: {}) => DELETE(`/scienceBase/cms/batch`, batch)
export const  getScienceBaseConditions = () => GET(`/scienceBase/cms/conditions`)
export const  getTableColumns = () => GET(`/scienceBase/cms/table-columns`)
export const  scienceBaseSelector = () => GET(`/scienceBase/cms/selector`)
export const  listBaseDisplayIndex = () => GET(`/scienceBase/cms/displayIndex`)
export const  putBaseDisplayIndex = (batch: Array<any>) => PUT(`/scienceBase/cms/changeIndex`, batch)
