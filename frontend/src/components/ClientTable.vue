<template>
  <v-card>
    <v-card-title>
      {{ title }}
    </v-card-title>
    <v-card-text>
      <v-progress-linear
        v-if="loading"
        indeterminate
        color="primary"
        class="mb-4"
      />

      <v-data-table
        v-else
        :items="clients"
        :headers="headers"
        :items-per-page="10"
        class="elevation-0"
        :loading="loading"
        loading-text="Loading clients..."
        no-data-text="No clients found"
      >
        <template v-slot:item.migrated="{ item }">
          <v-chip
            :color="item.migrated ? 'success' : 'warning'"
            size="small"
            variant="flat"
          >
            {{ item.migrated ? 'Migrated' : 'Pending' }}
          </v-chip>
        </template>

        <template v-slot:item.actions="{ item }">
          <v-btn
            color="primary"
            size="small"
            :disabled="item.migrated || migrating"
            :loading="migrating"
            @click="handleMigrate(item.id)"
          >
            <v-icon start>mdi-arrow-right</v-icon>
            Migrate
          </v-btn>
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: 'ClientTable',
  props: {
    clients: {
      type: Array,
      required: true,
      default: () => []
    },
    title: {
      type: String,
      required: true
    },
    showMigrateButton: {
      type: Boolean,
      default: false
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  emits: ['migrate'],
  data() {
    return {
      migrating: false
    };
  },
  computed: {
    headers() {
      const baseHeaders = [
        { title: 'ID', key: 'id', align: 'start', sortable: true },
        { title: 'Name', key: 'name', align: 'start', sortable: true },
        { title: 'Status', key: 'migrated', align: 'center', sortable: true }
      ];

      if (this.showMigrateButton) {
        baseHeaders.push({ title: 'Action', key: 'actions', align: 'center', sortable: false });
      }

      return baseHeaders;
    }
  },
  methods: {
    async handleMigrate(id) {
      this.migrating = true;
      try {
        await this.$emit('migrate', id);
      } finally {
        this.migrating = false;
      }
    }
  }
};
</script>
