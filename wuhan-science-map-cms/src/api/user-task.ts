import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  getUserTaskByTaskId = (taskId: string) => GET(`/userTask/web/${taskId}`)

export const  listUserTaskStatus = () => GET(`/userTask/web/status`)

export const  postUserTask = (params:{}) => POST(`/userTask/web`, params)
export const  putUserTask = (params:{}) => PUT(`/userTask/web`, params)

