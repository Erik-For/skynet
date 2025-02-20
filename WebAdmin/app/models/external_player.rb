class ExternalPlayer < ExternalDatabase
    self.table_name = "player"
    self.primary_key = "uuid"
end