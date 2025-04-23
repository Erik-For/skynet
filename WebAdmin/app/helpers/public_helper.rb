module PublicHelper

    def rank_color(rank)
        return case rank.downcase
            when 'admin' then 'bg-red-100 text-red-800'
            when 'management' then 'bg-fuchsia-100 text-fuchsia-600'
            when 'moderator' then 'bg-blue-100 text-blue-800'
            when 'mvp' then 'bg-green-100 text-green-800'
            else 'bg-gray-100 text-gray-800'
        end
    end

end
