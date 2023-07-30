import { GET } from 'boot/axios'


export const listRoles = () => GET(`/common/roles`)
export const listBaseTypes = () => GET(`/common/baseTypes`)
export const listNewsTypes = () => GET(`/common/newsTypes`)
export const listStatus = () => GET(`/common/status`)
