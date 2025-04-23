# app/controllers/public_controller.rb
class PublicController < ApplicationController
  def index
    @ranks = ["DEFAULT","MVP","MODERATOR","ADMIN","MANAGEMENT","WEAK_ADMIN"]
    @players = Player.all
    
    if params[:search].present?
      search_term = "%#{params[:search].downcase}%"
      @players = @players.where("LOWER(username) LIKE ? OR LOWER(nickname) LIKE ?", 
                               search_term, search_term)
    end

    if params[:ranks].present?
      ranks = params[:ranks].split(",")
      @players = @players.where(rank: ranks)
    end

    @players = sort_players(@players)

    respond_to do |format|
      format.html
      format.turbo_stream do
        render turbo_stream: turbo_stream.replace("player_table",
                partial: "public/players_table",
                locals: { players: @players })
      end
    end
  end

  def edit
    @ranks = ["DEFAULT","MVP","MODERATOR","ADMIN","MANAGEMENT","WEAK_ADMIN"]
    @player = Player.find_by(uuid: params[:id])
    render turbo_stream: turbo_stream.replace("edit_player_modal", partial: "public/edit")
  end
  
  def update
    @player = Player.find_by(uuid: params[:id])
    
    if @player.update(rank: params[:rank], nickname: params[:nickname], banned: params[:banned] == "1")
    else
      respond_to do |format|
        format.turbo_stream do
          render turbo_stream: turbo_stream.replace("edit_player_modal",
            partial: "public/edit",
            locals: { player: @player }),
            status: :unprocessable_entity
        end
      end
    end
  end

  private

  def sort_players(players)
    return players unless params[:sort].present?

    direction = %w[asc desc].include?(params[:direction]) ? params[:direction] : "asc"
    
    case params[:sort]
    when "uuid", "username", "rank", "playtime", "first_join", "last_seen", "banned", "nickname"
      players.order(params[:sort] => direction)
    else
      players
    end
  end
end