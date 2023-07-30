import instance from "./ajax";


export const loginApp = (id: string) => instance.get(`/user/app/login/wx/${id}`)
export const decryptMobileData = (encryptData: object) => instance.post(`/user/app/decryptData`, encryptData)
export const registerApp = (form: object) => instance.post(`/user/app/register/wx`, form)
export const logoutApp = () => instance.get(`/user/app/logout/wx`)
export const checkTokenApp = () => instance.get(`/user/app/checkToken`)
export const updateUserInfoApp = (form: object) => instance.put(`/user/app/wx`, form)

export const listUserFavoriteCourseByUserId = () => instance.get(`/userFavoriteCourse/app/`)
export const listUserRegisterCourseByUserId = () => instance.get(`/userRegisterCourse/app/`)

export const postUserCourseResult = (form: object) => instance.post(`/userUploadCourse/app/`, form)
export const listUserCourseResultByCourseId = (id: string) => instance.get(`/userUploadCourse/app/${id}`)