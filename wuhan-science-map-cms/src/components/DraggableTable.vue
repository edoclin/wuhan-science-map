<template>
  <div>
    <el-table id="table" row-key="id"
              :data="props.tableData" style="width: 100%">
      <el-table-column v-for="(item, index) in props.displayRows" :key="item.id" :prop="item.column"
                       :label="item.label"></el-table-column>
      <el-table-column fixed="right" label="备注">
        <template #header>
          <el-button type="primary" @click="submitChange">提交更改</el-button>
        </template>
        <template #default="scope">
          长按拖动可排序
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import Sortable from 'sortablejs'
import { ref } from 'vue'

const props = defineProps({
  tableData: {
    type: Array,
    default: []
  },
  displayRows: {
    type: Array,
    default: []
  },
})

const emit = defineEmits(['onDraggableChange', 'submitChange'])

onMounted(() => {
  console.log()
  let table = document.querySelector('.el-table__body-wrapper tbody')
  Sortable.create(table, {
    delay: 0,
    animation: 1000,
    onEnd: (e) => {
      const {
        newIndex,
        oldIndex
      } = e
      const currRow = props.tableData.splice(oldIndex, 1)[0];
      props.tableData.splice(newIndex, 0, currRow);
      emit('onDraggableChange', toRaw(props.tableData))
    }
  })
})

const submitChange = () => {
  emit('submitChange', toRaw(props.tableData))
}

</script>
