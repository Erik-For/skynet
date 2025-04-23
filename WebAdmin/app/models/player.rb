class Player < ExternalDatabase
    self.table_name = "player"
    self.primary_key = "uuid"

    def id
        self.UUID
    end

    def uuid
        self.UUID
    end
end