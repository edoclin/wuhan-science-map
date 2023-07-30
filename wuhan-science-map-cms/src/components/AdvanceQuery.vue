<template>
  <el-dialog :close-on-press-escape="false" :show-close="false" v-model="show" title="高级条件查询" destroy-on-close
             :close-on-click-modal="false">
    <el-form :model="conditions" label-width="120px">
      <el-form-item v-for="(item, index) in conditions" :key="index" :label="item.name">
        <el-input v-model="form[item.key]">
          <template #prefix v-if="index !== 0">
            <el-switch v-model="isAnd[item.key]" inline-prompt active-text="与" inactive-text="或"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="当前状态">
        <el-select v-model="form['status']" placeholder="请选择状态" style="width: 100%">
          <el-option v-for="status in status" :key="status.key" :label="status.value"
                     :value="status.key"/>
        </el-select>
      </el-form-item>
      <slot :formData="form" name="selector">
      </slot>
      <el-form-item label="创建时间">
        <el-date-picker format="YYYY-MM-DDTHH:mm:ss" v-model="form.createdBetween" type="datetimerange"
                        range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间"/>
      </el-form-item>
      <el-form-item label="更新时间">
        <el-date-picker format="YYYY-MM-DDTHH:mm:ss" v-model="form.updatedBetween" type="datetimerange"
                        range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间"/>
      </el-form-item>
    </el-form>
    <template #footer>
            <span class="dialog-footer">
                <el-button @click="onCancel">取消</el-button>
                <el-button type="primary" @click="onSubmit">确认</el-button>
            </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { useMapState } from 'src/stores'
import { useCommonStore } from 'stores/common_store'

const { status } = useMapState(useCommonStore, ['status'])
const props = defineProps({
  conditions: []
})
const form = reactive({
  selector: {}
})
const show = ref(true)
const isAnd = reactive({})

const emit = defineEmits(['onSubmit', 'onCancel'])
const onSubmit = () => {
  let result = []
  let createdBetween = {}
  let updatedBetween = {}
  let selector = {}
  if (form.createdBetween) {
    createdBetween = {
      start: form.createdBetween[0],
      end: form.createdBetween[1],
    }
    delete form.createdBetween
  }

  if (form.updatedBetween) {
    updatedBetween = {
      start: form.updatedBetween[0],
      end: form.updatedBetween[1],
    }
    delete form.updatedBetween
  }

  if (form.selector) {
    selector = { ...form.selector }
    Object.keys(form.selector).forEach(key => {
      delete form.selector[key]
    })
  }

  Object.keys(form).forEach(key => {
    if (key === 'selector') return
    result.push({
      column: key,
      value: form[key],
      isAnd: isAnd[key] === undefined ? false : isAnd[key]
    })
  })

  emit('onSubmit', {
    conditions: result,
    createdBetween,
    updatedBetween,
    selector
  })
}

const onCancel = () => {
  emit('onCancel', true)
}
</script>
