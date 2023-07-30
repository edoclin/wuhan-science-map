import instance from "./ajax";

export const  activityDetailById = (id: string) => instance.get(`/activity/app/detail/${id}`)