<template>
  <v-app>
    <v-app-bar color="primary" dark>
      <v-app-bar-title>
        Client Migration Tool
      </v-app-bar-title>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <v-row>
          <v-col cols="12" md="6">
            <ClientTable
              :clients="legacyClients"
              title="Legacy Clients"
              :show-migrate-button="true"
              :loading="loading"
              @migrate="migrateClient"
            />
          </v-col>

          <v-col cols="12" md="6">
            <ClientTable
              :clients="newClients"
              title="Migrated Clients"
              :loading="loading"
            />
          </v-col>
        </v-row>
      </v-container>

      <v-snackbar
        v-model="snackbar.show"
        :color="snackbar.color"
        :timeout="snackbar.timeout"
        location="bottom center"
      >
        {{ snackbar.message }}
        <template v-slot:actions>
          <v-btn
            variant="text"
            @click="snackbar.show = false"
          >
            Close
          </v-btn>
        </template>
      </v-snackbar>
    </v-main>
  </v-app>
</template>

<script>
import { clientService } from './services/api';
import ClientTable from './components/ClientTable.vue';

export default {
  name: 'App',
  components: {
    ClientTable
  },
  data() {
    return {
      legacyClients: [],
      newClients: [],
      loading: false,
      snackbar: {
        show: false,
        message: '',
        color: 'success',
        timeout: 4000
      }
    };
  },
  async mounted() {
    await this.loadClients();
  },
  methods: {
    showSnackbar(message, color = 'success', timeout = 4000) {
      this.snackbar.message = message;
      this.snackbar.color = color;
      this.snackbar.timeout = timeout;
      this.snackbar.show = true;
    },
    async loadClients() {
      this.loading = true;
      try {
        const [legacy, migrated] = await Promise.all([
          clientService.getLegacyClients(),
          clientService.getNewClients()
        ]);
        this.legacyClients = legacy;
        this.newClients = migrated;
      } catch (err) {
        const errorMessage = err.response?.data?.error || 'Failed to load clients';
        this.showSnackbar(errorMessage, 'error', 6000);
        console.error('Error loading clients:', err);
      } finally {
        this.loading = false;
      }
    },
    async migrateClient(id) {
      const client = this.legacyClients.find(c => c.id === id);
      const clientName = client ? client.name : `Client #${id}`;
      
      try {
        await clientService.migrateClient(id);
        await this.loadClients();
        this.showSnackbar(`Successfully migrated ${clientName}`, 'success', 4000);
      } catch (err) {
        const errorMessage = err.response?.data?.error || 'Failed to migrate client';
        this.showSnackbar(errorMessage, 'error', 6000);
        console.error('Error migrating client:', err);
      }
    }
  }
};
</script>
