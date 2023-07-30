<template>
  <div>
    <draggable-table :table-data="tableData" :display-rows="displayRows"
                     @onDraggableChange="handleDraggable" @submitChange="submitChangeIndex"></draggable-table>
  </div>
</template>

<script setup>

import DraggableTable from 'components/DraggableTable.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listBaseDisplayIndex, listScienceBase, putBaseDisplayIndex } from 'src/api/science-base'

onMounted(() => {
  ElMessageBox.alert('该功能可改变小程序端置顶基地的显示顺序, 长按数据项拖动即可改变顺序, 表格中的顺序即为小程序端的展示顺序', '基地排序提示', {
    confirmButtonText: '我知道了',
    callback: (action) => {
    },
  })
})
const tableData = ref([])
const queryParam = reactive({
  isAsc: false,
  orderColumns: ['createdTime'],
  conditions: [
    {
      'column': 'isTop',
      'value': true,
      'isAnd': true
    }
  ]
})

listBaseDisplayIndex().then(res => {
  tableData.value = res.data
})

const displayRows = ref([
  {
    column: 'baseName',
    label: '基地名称'
  },
])

const handleDraggable = (e) => {
}

const submitChangeIndex = (e) => {
  let batch = ref([])
  tableData.value.forEach((item, index) => {
    batch.value.push({
      id: item.id,
      displayIndex: index + 1
    })
  })
  putBaseDisplayIndex(batch.value).then(res => {
    ElMessage.success(res.data)
  })
}
</script>
<style scoped>

</style>
