import { defineStore } from 'pinia'

interface Status {
    key: string
    name: string
}

interface Role {
    key: string
    name: string
}
export const useCommonStore = defineStore('common_store', {
    state: () => ({
        status: [] as Array<Status>,
        roles: [] as Array<Role>,
        baseTypes: [] as Array<String>
    }),
    getters: {
    },
    actions: {
        updateStatus(status: Array<Status>) {
            this.status = status
        },
        updateRoles(roles: Array<Status>) {
            this.roles = roles
        },
        updateBaseTypes(baseTypes: Array<String>) {
            this.baseTypes = baseTypes
        },
    },
    persist: {
        enabled: true
    }
})


